package com.orar.Backend.Orar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RolTest {

    private Rol rol;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = mock(User.class);
        user2 = mock(User.class);

        rol = new Rol();
        rol.setId(1);
        rol.setName("STUDENT");
        rol.setUsers(List.of(user1, user2));
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1, rol.getId());
        assertEquals("STUDENT", rol.getName());
        assertNotNull(rol.getUsers());
        assertEquals(2, rol.getUsers().size());
        assertTrue(rol.getUsers().contains(user1));
    }

    @Test
    void testSettersWithNullValues() {
        rol.setName(null);
        rol.setUsers(null);

        assertNull(rol.getName());
        assertNull(rol.getUsers());
    }

    @Test
    void testSettersWithEmptyStrings() {
        rol.setName("");
        assertEquals("", rol.getName());
    }

    @Test
    void testSetIdSpecifically() {
        rol.setId(999);
        assertEquals(999, rol.getId());

        rol.setId(0);
        assertEquals(0, rol.getId());

        rol.setId(-1);
        assertEquals(-1, rol.getId());
    }

    @Test
    void testConstructorWithName() {
        Rol newRol = new Rol("ADMIN");
        assertEquals("ADMIN", newRol.getName());
        assertEquals(0, newRol.getId()); // Default int value
        assertNull(newRol.getUsers()); // Not set in constructor
    }

    @Test
    void testAllArgsConstructor() {
        List<User> users = List.of(user1, user2);
        Rol fullRol = new Rol(5, "ADMIN", users);

        assertEquals(5, fullRol.getId());
        assertEquals("ADMIN", fullRol.getName());
        assertEquals(2, fullRol.getUsers().size());
        assertTrue(fullRol.getUsers().contains(user1));
    }

    @Test
    void testNoArgsConstructor() {
        Rol emptyRol = new Rol();

        assertEquals(0, emptyRol.getId()); // int default
        assertNull(emptyRol.getName());
        assertNull(emptyRol.getUsers());
    }

    @Test
    void testUpdateName() {
        rol.setName("PROFESOR");
        assertEquals("PROFESOR", rol.getName());
    }

    @Test
    void testUsersListManipulation() {
        List<User> newUsers = new ArrayList<>();
        newUsers.add(user1);

        rol.setUsers(newUsers);
        assertEquals(1, rol.getUsers().size());
        assertTrue(rol.getUsers().contains(user1));

        // Test empty list
        rol.setUsers(new ArrayList<>());
        assertTrue(rol.getUsers().isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        Rol r1 = new Rol();
        r1.setId(5);

        Rol r2 = new Rol();
        r2.setId(5);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(rol, rol);
        assertTrue(rol.equals(rol));
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(rol, null);
        assertFalse(rol.equals(null));
    }

    @Test
    void testEqualsWithDifferentClass() {
        String differentObject = "not a rol";
        assertNotEquals(rol, differentObject);
        assertFalse(rol.equals(differentObject));
    }

    @Test
    void testEqualsWithDifferentId() {
        Rol r1 = new Rol();
        r1.setId(1);

        Rol r2 = new Rol();
        r2.setId(2);

        // Since no fields are explicitly included in equals/hashCode, all instances are equal
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsWithSameIdDifferentFields() {
        Rol r1 = new Rol();
        r1.setId(1);
        r1.setName("STUDENT");

        Rol r2 = new Rol();
        r2.setId(1);
        r2.setName("ADMIN");

        // Should be equal because @EqualsAndHashCode(onlyExplicitlyIncluded = true) with no explicitly included fields
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testEqualsWithBothZeroIds() {
        Rol r1 = new Rol();
        r1.setId(0);

        Rol r2 = new Rol();
        r2.setId(0);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = rol.hashCode();

        // Hash code should remain consistent across multiple calls
        assertEquals(initialHashCode, rol.hashCode());
        assertEquals(initialHashCode, rol.hashCode());

        // Changing fields should not affect hash code (due to @EqualsAndHashCode configuration)
        rol.setName("changedName");
        assertEquals(initialHashCode, rol.hashCode());
    }

    @Test
    void testHashCodeWithIdChange() {
        int initialHashCode = rol.hashCode();

        rol.setId(999);
        int newHashCode = rol.hashCode();

        // Since no fields are explicitly included in equals/hashCode, hashCode is constant
        assertEquals(initialHashCode, newHashCode);
    }

    @Test
    void testHashCodeWithZeroId() {
        Rol r = new Rol();
        r.setId(0);
        int hashCode = r.hashCode();

        // Should not throw exception and should be consistent
        assertEquals(hashCode, r.hashCode());
    }

    @Test
    void testCanEqualMethod() {
        Rol r1 = new Rol();
        Rol r2 = new Rol();

        assertTrue(r1.canEqual(r2));
        assertTrue(r2.canEqual(r1));

        String notRol = "not a rol";
        assertFalse(r1.canEqual(notRol));
    }

    @Test
    void testNullFields() {
        Rol r = new Rol();
        assertEquals(0, r.getId()); // int default
        assertNull(r.getName());
        assertNull(r.getUsers());
    }

    @Test
    void testRoleNameWithSpecialCharacters() {
        rol.setName("ADMIN&MODERATOR");
        assertEquals("ADMIN&MODERATOR", rol.getName());

        rol.setName("ROLE_USER");
        assertEquals("ROLE_USER", rol.getName());
    }

    @Test
    void testRoleNameWithUnicodeCharacters() {
        rol.setName("ADMINS_ROMÂNĂ");
        assertEquals("ADMINS_ROMÂNĂ", rol.getName());

        rol.setName("ROL_ÎNVĂȚĂTOR");
        assertEquals("ROL_ÎNVĂȚĂTOR", rol.getName());
    }

    @Test
    void testLongRoleName() {
        String longName = "Foarte foarte foarte lungă denumire de rol care depășește limitele normale și include multe cuvinte pentru testare";
        rol.setName(longName);
        assertEquals(longName, rol.getName());
    }

    @Test
    void testCompleteRolSetup() {
        List<User> newUsers = List.of(
                mock(User.class),
                mock(User.class),
                mock(User.class)
        );

        Rol completeRol = new Rol();
        completeRol.setId(200);
        completeRol.setName("SUPER_ADMIN");
        completeRol.setUsers(newUsers);

        // Verify all fields are set correctly
        assertEquals(200, completeRol.getId());
        assertEquals("SUPER_ADMIN", completeRol.getName());
        assertEquals(3, completeRol.getUsers().size());
    }

    @Test
    void testToStringNotNull() {
        assertNotNull(rol.toString());

        Rol emptyRol = new Rol();
        assertNotNull(emptyRol.toString());
    }

    @Test
    void testRoleTypicalUseCases() {
        // Test common role names
        String[] commonRoles = {
                "STUDENT",
                "PROFESOR",
                "ADMIN",
                "MODERATOR",
                "USER",
                "SUPER_ADMIN",
                "GUEST"
        };

        for (String roleName : commonRoles) {
            rol.setName(roleName);
            assertEquals(roleName, rol.getName());
        }
    }

    @Test
    void testUsersWithLargeNumberOfElements() {
        // Test with large users list
        List<User> largeUsers = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            largeUsers.add(mock(User.class));
        }

        rol.setUsers(largeUsers);
        assertEquals(100, rol.getUsers().size());
    }

    @Test
    void testConstructorWithEmptyName() {
        Rol emptyNameRol = new Rol("");
        assertEquals("", emptyNameRol.getName());
    }

    @Test
    void testConstructorWithNullName() {
        Rol nullNameRol = new Rol((String) null);
        assertNull(nullNameRol.getName());
    }

    @Test
    void testIdPrimitiveInt() {
        // Test that id is primitive int (not Integer)
        Rol newRol = new Rol();
        assertEquals(0, newRol.getId()); // Default value for int

        newRol.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, newRol.getId());

        newRol.setId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, newRol.getId());
    }
}
