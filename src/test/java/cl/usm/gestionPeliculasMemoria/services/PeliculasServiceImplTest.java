package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    @Mock
    PeliculasRepository peliculasRepository;

    @InjectMocks
    PeliculasServiceImpl peliculasService;

    private Pelicula p1;
    private Pelicula p2;
    private Comentario comentario1;
    private Comentario comentario2;
    private String idInexistente = "NO_EXISTE";

    @BeforeEach
    void setUp(){
        p1 = new Pelicula("Peli1", "Inception", "ChrisNol", "token1", null);
        p2 = new Pelicula("Peli2", "Interstellar", "ChrisNol", "token2", null);
        comentario1 = new Comentario("juan_perez", "Excelente pelicula");
        comentario2 = new Comentario("sancha_polen", "Mala pelicula. Horrible.");;
    }

    @Test
    void createPelicula_Exito_GeneraTokenEInserta() {
        when(peliculasRepository.insert(any(Pelicula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pelicula resultado = peliculasService.createPelicula(p1);

        assertNotNull(resultado);
        assertEquals("Peli1", resultado.getId());
        assertEquals("Inception", resultado.getTitulo());

        assertNotNull(resultado.getTokenDescarga());
        assertEquals(10, resultado.getTokenDescarga().length());

        assertTrue(resultado.getTokenDescarga().matches("^[a-zA-Z0-9]+$"));

        verify(peliculasRepository, Mockito.times(1)).insert(p1);
    }

    @Test
    void createPelicula_FalloRepository_RetornaNull() {
        when(peliculasRepository.insert(any(Pelicula.class))).thenThrow(new IllegalArgumentException("ID Duplicado"));

        Pelicula resultado = peliculasService.createPelicula(p1);


        assertNull(resultado);

        verify(peliculasRepository, Mockito.times(1)).insert(p1);
    }

    @Test
    void getAll_Exito_RetornaListaCompleta() {
        List<Pelicula> peliculasMock = Arrays.asList(p1, p2);

        when(peliculasRepository.findAll()).thenReturn(peliculasMock);

        List<Pelicula> resultado = peliculasService.getAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Inception", resultado.get(0).getTitulo());
        assertEquals("Interstellar", resultado.get(1).getTitulo());

        verify(peliculasRepository, Mockito.times(1)).findAll();
    }

    @Test
    void filter_VariosCriterios_RetornaPeliculasFiltradas() {
        List<Pelicula> catalogoCompleto = Arrays.asList(p1, p2);
        when(peliculasRepository.findAll()).thenReturn(catalogoCompleto);

        List<Pelicula> filtroTitulo = peliculasService.filter("Inter");
        assertEquals(1, filtroTitulo.size());
        assertEquals("Interstellar", filtroTitulo.get(0).getTitulo());

        List<Pelicula> filtroId = peliculasService.filter("Peli1");
        assertEquals(1, filtroId.size());
        assertEquals("Inception", filtroId.get(0).getTitulo());

        List<Pelicula> filtroMayusculas = peliculasService.filter("INCEPTION");
        assertEquals(1, filtroMayusculas.size());
        assertEquals("Inception", filtroMayusculas.get(0).getTitulo());

        List<Pelicula> filtroSinCoincidencias = peliculasService.filter("xyz");
        assertTrue(filtroSinCoincidencias.isEmpty());
    }


    @Test
    void findById_ExistePelicula_RetornaPelicula() {
        String idExistente = p1.getId();
        when(peliculasRepository.findById(idExistente)).thenReturn(p1);

        Pelicula resultado = peliculasService.findById(idExistente);

        assertNotNull(resultado);
        assertEquals(idExistente, resultado.getId());
        assertEquals(p1.getTitulo(), resultado.getTitulo());

        verify(peliculasRepository, Mockito.times(1)).findById(idExistente);
    }

    @Test
    void findById_NoExistePelicula_RetornaNull() {

        when(peliculasRepository.findById(idInexistente)).thenReturn(null);

        Pelicula resultado = peliculasService.findById(idInexistente);

        assertNull(resultado);

        verify(peliculasRepository, Mockito.times(1)).findById(idInexistente);
    }

    @Test
    void filter() {
    }
}