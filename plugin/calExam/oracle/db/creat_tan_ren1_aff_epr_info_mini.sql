drop table REN1_aff_epr_info_mini;
create table REN1_aff_epr_info_mini
(
    COD_CIN                VARCHAR2(10)            not null,
    LIB_INF_MIN            VARCHAR2(800)           not null    ,
    constraint PK_REN1_aff_epr_info_mini primary key (COD_CIN)
 using index 
 tablespace INDX_APO
 storage
 (
 initial 10 K
 next 10 K
 pctincrease 25
 )
)
tablespace DATA_APO
storage
(
initial 10K
next 10K
pctincrease 25
);

alter table REN1_aff_epr_info_mini
    add constraint FK_REN1_epr_inf_mini_cin foreign key  (COD_CIN)
       references APOGEE.centre_incomp (COD_CIN);
create public synonym REN1_aff_epr_info_mini for REN1_aff_epr_info_mini;
grant select on REN1_aff_epr_info_mini to role_apogee_read;
grant select, insert, update, delete on REN1_aff_epr_info_mini to role_apogee_write;
/