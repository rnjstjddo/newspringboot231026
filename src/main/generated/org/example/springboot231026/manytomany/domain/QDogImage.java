package org.example.springboot231026.manytomany.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDogImage is a Querydsl query type for DogImage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDogImage extends EntityPathBase<DogImage> {

    private static final long serialVersionUID = -1314999119L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDogImage dogImage = new QDogImage("dogImage");

    public final QDog dog;

    public final StringPath imgName = createString("imgName");

    public final NumberPath<Long> inum = createNumber("inum", Long.class);

    public final StringPath path = createString("path");

    public final StringPath uuid = createString("uuid");

    public QDogImage(String variable) {
        this(DogImage.class, forVariable(variable), INITS);
    }

    public QDogImage(Path<? extends DogImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDogImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDogImage(PathMetadata metadata, PathInits inits) {
        this(DogImage.class, metadata, inits);
    }

    public QDogImage(Class<? extends DogImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dog = inits.isInitialized("dog") ? new QDog(forProperty("dog")) : null;
    }

}

