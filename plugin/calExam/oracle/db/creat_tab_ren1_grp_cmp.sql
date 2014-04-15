-- ============================================================
--   Table : REN1_GRP_CMP                                      
-- ============================================================
drop table REN1_GRP_CMP;
create table REN1_GRP_CMP
(
    COD_GRP          VARCHAR2(8)            not null,
    LIB_GRP          VARCHAR2(35)           not null,
    TEM_EN_SVE_GRP   VARCHAR2(1)            default 'O' not null
        constraint CKC_TEM_EN_SVE_GRP_REN1_GRP check (TEM_EN_SVE_GRP in ('O','N')),
    constraint PK_REN1_GRP_CMP primary key (COD_GRP)
        using index
        tablespace INDX_APO
        storage
        (
        initial 100K
        next 100K
        pctincrease 50
     )
)
tablespace DATA_APO
storage
(
initial 100K
next 100K
pctincrease 50
);

-- ============================================================
--   Table : REN1_GRP_CMP_CMP                                  
-- ============================================================
drop table REN1_GRP_CMP_CMP;
create table REN1_GRP_CMP_CMP
(
    COD_CMP          VARCHAR2(3)            not null,
    COD_GRP          VARCHAR2(8)            not null,
    constraint PK_REN1_GRP_CMP_CMP primary key (COD_CMP, COD_GRP)
        using index
        tablespace INDX_APO
        storage
        (
        initial 100K
        next 100K
        pctincrease 50
     )
)
tablespace DATA_APO
storage
(
initial 100K
next 100K
pctincrease 50
);

alter table REN1_GRP_CMP_CMP
    add constraint FK_REN1_GRP_LIEN_24_COMPOSAN foreign key  (COD_CMP)
       references apogee.COMPOSANTE (COD_CMP);
alter table REN1_GRP_CMP_CMP
    add constraint FK_REN1_GRP_LIEN_25_REN1_GRP foreign key  (COD_GRP)
       references REN1_GRP_CMP (COD_GRP);
create public synonym REN1_GRP_CMP for REN1_GRP_CMP;
grant select on REN1_GRP_CMP to ROLE_APOGEE_READ;
grant select, insert, update, delete on REN1_GRP_CMP to ROLE_APOGEE_WRITE;
create public synonym REN1_GRP_CMP_CMP for REN1_GRP_CMP_CMP;
grant select on REN1_GRP_CMP_CMP to ROLE_APOGEE_READ;
grant select, insert, update, delete on REN1_GRP_CMP_CMP to ROLE_APOGEE_WRITE;
/
