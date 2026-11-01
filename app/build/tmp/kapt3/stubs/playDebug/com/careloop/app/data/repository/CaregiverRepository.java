package com.careloop.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u0006H\u0086@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0012\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000e0\rJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\u0010\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\f\u0010\u0011\u001a\u00020\n*\u00020\u0012H\u0002J\f\u0010\u0013\u001a\u00020\u0012*\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/careloop/app/data/repository/CaregiverRepository;", "", "caregiverDao", "Lcom/careloop/app/data/local/dao/CaregiverDao;", "(Lcom/careloop/app/data/local/dao/CaregiverDao;)V", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCaregiver", "caregiver", "Lcom/careloop/app/domain/model/Caregiver;", "(Lcom/careloop/app/domain/model/Caregiver;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCaregivers", "Lkotlinx/coroutines/flow/Flow;", "", "getCaregiversList", "insertCaregiver", "toDomain", "Lcom/careloop/app/data/local/entity/CaregiverEntity;", "toEntity", "app_playDebug"})
public final class CaregiverRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.local.dao.CaregiverDao caregiverDao = null;
    
    public CaregiverRepository(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.dao.CaregiverDao caregiverDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.domain.model.Caregiver>> getAllCaregivers() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCaregiversList(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.Caregiver>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertCaregiver(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.Caregiver caregiver, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteCaregiver(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.Caregiver caregiver, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.careloop.app.domain.model.Caregiver toDomain(com.careloop.app.data.local.entity.CaregiverEntity $this$toDomain) {
        return null;
    }
    
    private final com.careloop.app.data.local.entity.CaregiverEntity toEntity(com.careloop.app.domain.model.Caregiver $this$toEntity) {
        return null;
    }
}