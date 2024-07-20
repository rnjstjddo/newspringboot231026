package org.example.springboot231026.manytomany.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = 1383805642L;

    public static final QDog dog = new QDog("dog");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final NumberPath<Double> age = createNumber("age", Double.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> dno = createNumber("dno", Long.class);

    public final StringPath health = createString("health");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath other = createString("other");

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public final StringPath writer = createString("writer");

    public QDog(String variable) {
        super(Dog.class, forVariable(variable));
    }

    public QDog(Path<? extends Dog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDog(PathMetadata metadata) {
        super(Dog.class, metadata);
    }

}

