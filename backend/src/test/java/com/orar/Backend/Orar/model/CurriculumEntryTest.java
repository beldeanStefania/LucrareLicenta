package com.orar.Backend.Orar.model;

import com.orar.Backend.Orar.enums.Tip;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CurriculumEntryTest {

    @Test
    void testEqualsAndHashCode() {
        CurriculumEntry c1 = new CurriculumEntry();
        c1.setId(1);
        c1.setAn(1);
        c1.setSemestru(1);
        c1.setTip(Tip.OBLIGATORIE);

        CurriculumEntry c2 = new CurriculumEntry();
        c2.setId(1);
        c2.setAn(2);
        c2.setSemestru(2);
        c2.setTip(Tip.FACULTATIVA);

        CurriculumEntry c3 = new CurriculumEntry();
        c3.setId(2);

        assertEquals(c1, c2); // id egal ⇒ obiectele sunt considerate egale
        assertEquals(c1.hashCode(), c2.hashCode());

        assertNotEquals(c1, c3);
        assertNotEquals(c1.hashCode(), c3.hashCode());
    }

    @Test
    void testSettersAndGetters() {
        CurriculumEntry entry = new CurriculumEntry();

        Specializare specializare = new Specializare();
        Materie materie = new Materie();
        MateriiOptionale optionale = new MateriiOptionale();

        entry.setId(100);
        entry.setAn(3);
        entry.setSemestru(2);
        entry.setTip(Tip.OPTIONALA);
        entry.setSpecializare(specializare);
        entry.setMaterie(materie);
        entry.setOptionale(optionale);

        assertEquals(100, entry.getId());
        assertEquals(3, entry.getAn());
        assertEquals(2, entry.getSemestru());
        assertEquals(Tip.OPTIONALA, entry.getTip());
        assertSame(specializare, entry.getSpecializare());
        assertSame(materie, entry.getMaterie());
        assertSame(optionale, entry.getOptionale());
    }
}
