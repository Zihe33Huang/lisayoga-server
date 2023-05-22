package com.lht.tams;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.junit.Test;

public class IdTest {

    @Test
    public void testIdWord(){
        System.out.println(IdWorker.getId());
        System.out.println(IdWorker.getIdStr());
        System.out.println(IdWorker.getIdStr().length());
        System.out.println(IdWorker.get32UUID());

    }

}
