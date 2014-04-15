drop table REN1_AFF_RENT_INFO_MINI;
create table REN1_AFF_RENT_INFO_MINI
(
	COD_CGE VARCHAR2(3) not null,
	LIB_INF_MINI VARCHAR2(800),
 constraint PK_REN1_AFF_RENT_INFO_MINI primary key (COD_CGE)
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
);
/
alter table REN1_AFF_RENT_INFO_MINI
add constraint FK_REN1_AFF_RENT_INFO_MINI foreign key (COD_CGE)
	references APOGEE.CENTRE_GESTION (COD_CGE);
/
create public synonym REN1_AFF_RENT_INFO_MINI for REN1_AFF_RENT_INFO_MINI;
grant select on REN1_AFF_RENT_INFO_MINI to role_apogee_read;
grant select, insert, update, delete on REN1_AFF_RENT_INFO_MINI to role_apogee_write;