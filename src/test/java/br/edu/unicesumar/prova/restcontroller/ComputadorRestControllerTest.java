package br.edu.unicesumar.prova.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.testng.Assert.*;

public class ComputadorRestControllerTest {

    @Autowired
    public MockMvc mvc;

    @SneakyThrows
    @Test
    public void testCadastrar() {
//        String contentAsString = mvc.perform(get("/api/computador")).andReturn().getResponse().getContentAsString();
//        System.out.println(contentAsString);
    }

    @Test
    public void testFindById() {
    }

    @Test
    public void testListAll() {
    }

    @Test
    public void testEditar() {
    }

    @Test
    public void testDelete() {
    }
}