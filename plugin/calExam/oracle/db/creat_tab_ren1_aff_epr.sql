drop table DEVAPO.REN1_AFF_EPR;
create table DEVAPO.REN1_AFF_EPR
(
 COD_PES NUMBER(9) not null,
 COD_ANU VARCHAR2(4) not null,
 COD_CLN VARCHAR2(10) not null,
 COD_ETP VARCHAR2(6) not null,
 COD_PXA VARCHAR2(10) not null,
 COD_VRS_VET NUMBER(3) not null,
 COD_EPR VARCHAR2(10) null,
 DAT_DEB_AFF DATE not null,
 DAT_DEB_PES DATE not null,
 DAT_FIN_AFF DATE not null,
 DHH_DEB_PES NUMBER(2) not null,
 DMM_DEB_PES NUMBER(2) not null,
 DUR_EXA_EPR_PES NUMBER(5) not null,
 LIB_BATIMENT VARCHAR2(40) not null,
 LIB_EPR VARCHAR2(40) not null ,
 LIB_PXA VARCHAR2(40) not null,
 LIB_SALLE VARCHAR2(40) not null,
 NBR_ETU_INS_PES NUMBER(5) not null,
 COD_NEP VARCHAR2(4) not null,
 DAT_MAJ DATE not null,
 TEM_TRS_PES VARCHAR2(1) default 'N' not null
 constraint CKC_TEM_TRS_PES_REN1_AFF check (
 TEM_TRS_PES in ('O','N')),
 constraint PK_REN1_AFF_EPR primary key (COD_PES, COD_ETP, COD_VRS_VET )
 using index
 tablespace INDX_APO
 storage
 (
 initial 1000K
 next 1000K
 pctincrease 50
 )
)
tablespace DATA_APO
storage
(
initial 1000K
next 1000K
pctincrease 50
);
CREATE or REPLACE trigger ren1_maj_dat_aff_epr
 BEFORE INSERT OR UPDATE
 ON ren1_aff_epr
 FOR EACH ROW 
  BEGIN
    :New.dat_maj := Sysdate;
   END;
create public synonym REN1_AFF_EPR for REN1_AFF_EPR;
grant select on REN1_AFF_EPR to role_apogee_read;
grant select, insert, update, delete on REN1_AFF_EPR to role_apogee_write;
/