const test = require('node:test');
const assert = require('node:assert/strict');
const http = require('node:http');
const { app, sanitizeOutboundMessage } = require('../escalation_service');

let server;
let baseUrl;

test.before((t, done) => {
    server = http.createServer(app);
    server.listen(0, () => {
        const port = server.address().port;
        baseUrl = `http://127.0.0.1:${port}`;
        done();
    });
});

test.after((t, done) => {
    server.close(done);
});

test('sanitizeOutboundMessage strictly strips drug names and enforces data-minimisation', () => {
    const msgWithDrug = sanitizeOutboundMessage('Alice', 'Take Kardegic 75mg sachet');
    assert.equal(msgWithDrug, 'Alice has not confirmed an important reminder.');

    const msgWithChemo = sanitizeOutboundMessage('Robert', 'Chemo infusion 500mg');
    assert.equal(msgWithChemo, 'Robert has not confirmed an important reminder.');

    const defaultMsg = sanitizeOutboundMessage(null, null);
    assert.equal(defaultMsg, 'Your family member has not confirmed an important reminder.');
});

test('GET /health returns HDS-compliant stateless status', async () => {
    const res = await fetch(`${baseUrl}/health`);
    assert.equal(res.status, 200);
    const body = await res.json();
    assert.equal(body.status, 'ok');
    assert.equal(body.hdsCompliant, true);
    assert.equal(body.stateless, true);
    assert.equal(body.dataMinimised, true);
});

test('POST /api/escalate rejects request missing caregiverPhone', async () => {
    const res = await fetch(`${baseUrl}/api/escalate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ patientName: 'Jean' })
    });
    assert.equal(res.status, 400);
    const body = await res.json();
    assert.match(body.error, /Missing required field: caregiverPhone/);
});

test('POST /api/escalate dispatches data-minimised alert and simulates Twilio in demo mode', async () => {
    const res = await fetch(`${baseUrl}/api/escalate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            caregiverName: 'Claire',
            caregiverPhone: '+33612345678',
            patientName: 'Jean Dupont',
            commitmentTitle: 'Take Kardegic 75mg',
            channel: 'SMS'
        })
    });

    assert.equal(res.status, 200);
    const body = await res.json();
    assert.equal(body.success, true);
    assert.equal(body.simulated, true);
    assert.equal(body.channel, 'SMS');
    assert.equal(body.outboundMessage, 'Jean Dupont has not confirmed an important reminder.');
    assert.equal(body.outboundMessage.includes('Kardegic'), false);
    assert.equal(body.outboundMessage.includes('75mg'), false);
});

test('POST /api/fcm/relay handles stateless push notification forwarding', async () => {
    const res = await fetch(`${baseUrl}/api/fcm/relay`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            pairingToken: 'PAIR-98765',
            caregiverFcmToken: 'fcm-mock-token-abc',
            patientName: 'Jean Dupont',
            alertMessage: 'Missed Kardegic',
            priority: 'HIGH'
        })
    });

    assert.equal(res.status, 200);
    const body = await res.json();
    assert.equal(body.success, true);
    assert.equal(body.relayed, true);
    assert.equal(body.pairingToken, 'PAIR-98765');
    assert.equal(body.outboundMessage, 'Jean Dupont has not confirmed an important reminder.');
    assert.equal(body.outboundMessage.includes('Kardegic'), false);
});

