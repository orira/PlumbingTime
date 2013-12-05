package com.rsd.plumbing.domain;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

import javax.inject.Singleton;

/**
 * Created by wadereweti on 27/11/13.
 */

@Singleton
@Table(name = "Pipe")
public class Pipe extends Model{

    @Column(name = "size")
    public int size;

    @Column(name = "gradient")
    public int gradient;

    @Column(name = "image_name")
    public String imageName;

    public static List<Pipe> findAll() {
        return new Select().from(Pipe.class).execute();
    }
}
