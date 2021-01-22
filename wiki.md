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
  
* To avoid infinite recursive (stackoverflow)  
use jackson annotation JsonIdentityInfo as below at the referenced side
  
```
@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Parent {}
```
* fetchType.EAGER can solve sql N+ 1 problem, but may cause duplicate result.
```Hibernate: 
    select
        parent0_.id as id1_1_0_,
        parent0_.name as name2_1_0_,
        childs1_.parent_id as parent_i3_0_1_,
        childs1_.id as id1_0_1_,
        childs1_.id as id1_0_2_,
        childs1_.name as name2_0_2_,
        childs1_.parent_id as parent_i3_0_2_ 
    from
        parent parent0_ 
    left outer join
        child childs1_ 
            on parent0_.id=childs1_.parent_id 
    where
        parent0_.id=?
```