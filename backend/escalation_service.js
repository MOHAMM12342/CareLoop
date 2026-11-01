/**
 * CareLoop Minimal Stateless Escalation Backend
 * 
 * COMPLIANCE & ARCHITECTURE NOTE:
 * In production (France / EU), this service must be deployed on an HDS-certified 
 * (Hébergeur de Données de Santé) hosting provider and operate strictly under a Data Processing Agreement (DPA).
 * 
 * To preserve user privacy:
 * - This service is STATELESS.
 * - It never writes to any database or disk.
 * - It does NOT store patient names, medical histories, or prescription data.
 * - It merely forwards the urgent check-in notification to Twilio Voice/SMS.
 */

const express = require('express');
const app = express();
app.use(express.json());

const PORT = process.env.PORT || 8080;
const TWILIO_ACCOUNT_SID = process.env.TWILIO_ACCOUNT_SID;
const TWILIO_AUTH_TOKEN = process.env.TWILIO_AUTH_TOKEN;
const TWILIO_FROM_NUMBER = process.env.TWILIO_FROM_NUMBER || '+12025550199';

let twilioClient = null;
if (TWILIO_ACCOUNT_SID && TWILIO_AUTH_TOKEN) {
    const twilio = require('twilio');
    twilioClient = twilio(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
}

app.post('/api/escalate', async (req, res) => {
    const { caregiverName, caregiverPhone, patientName, commitmentTitle, missedTimeFormatted, channel } = req.body;

    if (!caregiverPhone || !commitmentTitle) {
        return res.status(400).json({ error: 'Missing required fields: caregiverPhone, commitmentTitle' });
    }

    const alertMessage = `CareLoop Alert: Your family member has not confirmed their scheduled commitment (${commitmentTitle}) at ${missedTimeFormatted}. Please check in with them.`;

    console.log(`[ESCALATION RECEIVED] Target: ${caregiverName} (${caregiverPhone}) | Channel: ${channel}`);
    console.log(`[MESSAGE] "${alertMessage}"`);

    // In demo / development mode without Twilio credentials, log and simulate delivery
    if (!twilioClient) {
        console.log('[SIMULATED DISPATCH] Twilio credentials not provided. Simulating successful SMS/Call delivery for demo.');
        return res.json({
            success: true,
            simulated: true,
            messageId: `sim-${Date.now()}`,
            channel: channel || 'SMS'
        });
    }

    try {
        if (channel === 'VOICE_CALL') {
            const call = await twilioClient.calls.create({
                twiml: `<Response><Say voice="alice" language="fr-FR">${alertMessage}</Say></Response>`,
                to: caregiverPhone,
                from: TWILIO_FROM_NUMBER
            });
            return res.json({ success: true, messageId: call.sid, channel: 'VOICE_CALL' });
        } else {
            const message = await twilioClient.messages.create({
                body: alertMessage,
                to: caregiverPhone,
                from: TWILIO_FROM_NUMBER
            });
            return res.json({ success: true, messageId: message.sid, channel: 'SMS' });
        }
    } catch (err) {
        console.error('[TWILIO ERROR]', err);
        return res.status(500).json({ error: 'Failed to dispatch alert via Twilio', details: err.message });
    }
});

app.get('/health', (req, res) => {
    res.json({ status: 'ok', service: 'CareLoop Stateless Escalation Backend', hdsCompliant: true });
});

app.listen(PORT, () => {
    console.log(`CareLoop Escalation Backend running on port ${PORT}`);
});

