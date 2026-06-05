package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PeliculasRepositoryImplTest {

    private PeliculasRepositoryImpl repository;
    private Pelicula p1;

    @BeforeEach
    void setUp() {
        repository = new PeliculasRepositoryImpl();
        p1 = new Pelicula("Peli1", "Inception", "ChrisNol", "token1", null);
    }

    @Test
    void insert_Exito_InsertaYRetornaPelicula() {
        Pelicula resultado = repository.insert(p1);

        assertNotNull(resultado);
        assertEquals("Peli1", resultado.getId());

        List<Pelicula> todas = repository.findAll();
        assertEquals(1, todas.size());
        assertEquals("Inception", todas.get(0).getTitulo());
    }

    @Test
    void insert_IdNulo_LanzaExcepcion() {
        Pelicula peliculaIdNulo = new Pelicula(null, "Titulo", "Director", "tok", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.insert(peliculaIdNulo);
        });

        assertEquals("El ID de la pelicula no puede ser nulo", exception.getMessage());
    }

    @Test
    void insert_IdDuplicado_LanzaExcepcion() {
        repository.insert(p1);

        Pelicula peliculaDuplicada = new Pelicula("Peli1", "Otro Titulo", "Otro Director", "tok2", null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            repository.insert(peliculaDuplicada);
        });

        assertEquals("La pelicula con ID Peli1 ya existe", exception.getMessage());
    }

    @Test
    void findAll_Vacio_RetornaListaVacia() {
        List<Pelicula> resultado = repository.findAll();
        assertTrue(resultado.isEmpty());
    }

    @Test
    void findById_ExistePelicula_RetornaPelicula() {
        repository.insert(p1);

        Pelicula encontrada = repository.findById("Peli1");

        assertNotNull(encontrada);
        assertEquals("Peli1", encontrada.getId());
    }

    @Test
    void findById_NoExisteONulo_RetornaNull() {
        repository.insert(p1);

        assertNull(repository.findById("INEXISTENTE"));
        assertNull(repository.findById(null));
    }
}