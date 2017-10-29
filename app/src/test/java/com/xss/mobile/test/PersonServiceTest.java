package com.xss.mobile.test;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by xss on 2017/3/23.
 */
public class PersonServiceTest {

    private PersonDao mockDao;

    private PersonService personService;

    @Mock
    View view;

    @Before
    public void setUp() throws Exception {
        // 模拟 PersonDao
        mockDao = mock(PersonDao.class);

        // 当传入 getPerson传入1时，设置一个 Person = new Person(1, "Person1");
        when(mockDao.getPerson(1)).thenReturn(new Person(1, "Person1"));
        when(mockDao.update(isA(Person.class))).thenReturn(true);

        personService = new PersonService(mockDao);
    }


    @Test
    public void update() throws Exception {
        boolean result = personService.update(1, "new name");
        assertTrue("must true", result);

        // 验证是否已经执行过一次 getPerson(1)
        verify(mockDao, times(1)).getPerson(eq(1));

        verify(mockDao, times(1)).update(isA(Person.class));
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        boolean result = personService.update(2, "new name");
        assertFalse("must true", result);

        verify(mockDao, times(1)).getPerson(eq(1));
        verify(mockDao, never()).update(isA(Person.class));
    }

}