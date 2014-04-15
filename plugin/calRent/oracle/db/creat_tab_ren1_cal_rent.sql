drop table REN1_CAL_RENT;
create table REN1_CAL_RENT
(
 COD_CGE VARCHAR2(3) not null,
 COD_ETP VARCHAR2(6) not null,
 COD_VRS_VET NUMBER(3) not null,
 DAT_DEB DATE,
 HRE_DEB number(2),
 MIN_DEB number(2)
 LIB_LIEU VARCHAR2(82),
 TEM_AFF_RENT VARCHAR2(1) default 'N' not null
          constraint CKC_TEM_AFF_RENT_REN1_CAL_RENT check 
( TEM_AFF_RENT in ('O','N')),
 COMMENTAIRE VARCHAR2(100),
constraint PK_REN1_CAL_RENT primary key (COD_CGE, COD_ETP, COD_VRS_VET )
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
 
/
alter table REN1_CAL_RENT
add constraint FK_REN1_CAL_RENT_CGE foreign key (COD_CGE)
    references APOGEE.CENTRE_GESTION (COD_CGE);
/

alter table REN1_CAL_RENT
    add constraint
FK_REN1_CAL_RENT_ETP foreign key(COD_ETP,COD_VRS_VET)
references APOGEE.VERSION_ETAPE(COD_ETP,COD_VRS_VET);
/

create public synonym ren1_cal_rent for ren1_cal_rent;
grant select on ren1_cal_rent to role_apogee_read;
grant select, insert, update, delete on ren1_cal_rent to role_apogee_write;