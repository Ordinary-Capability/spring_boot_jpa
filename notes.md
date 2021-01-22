### Owing Side
- what is owning side in ORM(object-relational mapping)  
the table owns the foreign key is the "owning side", this usually the "many" side
  

### JPA with hibernate example
- Two entities without declare an owning side

```
@Entity
public class Child {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;
private String name;
@ManyToOne
private Parent parent;
}
@Entity
class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany
    private Set<Child> childSet;
}

```
Hibernate sql statement output:
```
Hibernate: 
    
    create table child (
       id integer not null,
        name varchar(255),
        parent_id integer,
        primary key (id)
    ) engine=InnoDB

Hibernate: 
    
    create table parent (
       id integer not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB
    
Hibernate: 
    
    create table parent_child_set (
       parent_id integer not null,
        child_set_id integer not null,
        primary key (parent_id, child_set_id)
    ) engine=InnoDB
Hibernate: 
    
    alter table parent_child_set 
       add constraint UK_tio100rd7g0wh1b1xirl44uyq unique (child_set_id)
Hibernate: 
    
    alter table child 
       add constraint FK7dag1cncltpyhoc2mbwka356h 
       foreign key (parent_id) 
       references parent (id)
Hibernate: 
    
    alter table parent_child_set 
       add constraint FKnn9ev3ubkm5b6sedagsnwtciq 
       foreign key (child_set_id) 
       references child (id)
Hibernate: 
    
    alter table parent_child_set 
       add constraint FK369twm4t9xum79prghgdd0oa2 
       foreign key (parent_id) 
       references parent (id)
```
- Two entities with the parent declare the owning side
```
@Entity
public class Child {
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;
private String name;
@ManyToOne
private Parent parent;
}
@Entity
class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "parent")
    private Set<Child> childSet;
}

```
Hibernate sql statement output:
```
Hibernate: 
    create table child (
       id integer not null,
        name varchar(255),
        parent_id integer,
        primary key (id)
    ) engine=InnoDB

Hibernate: 
    create table parent (
       id integer not null,
        name varchar(255),
        primary key (id)
    ) engine=InnoDB
Hibernate: 
    
    alter table child 
       add constraint FK7dag1cncltpyhoc2mbwka356h 
       foreign key (parent_id) 
       references parent (id)
```

- JoinColumn vs MappedBy  
JoinColumn only specify the foreign key column name, it not affects the track relationship.