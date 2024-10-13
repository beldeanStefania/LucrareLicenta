//package com.orar.Backend.Orar.service;
//
//import com.orar.Backend.Orar.dto.OrarOraDTO;
//import com.orar.Backend.Orar.model.OrarOra;
//import com.orar.Backend.Orar.repository.OrarOraRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class OrarOraService {
//
//    @Autowired
//    private OrarOraRepository orarOraRepository;
//
//    public List<OrarOra> getAll() {
//        return orarOraRepository.findAll();
//    }
//
//    public OrarOra add(final OrarOraDTO orarOraDTO) {
//
//       //vf daca exista orarul si ora
//        //build
//        //add
//    }
//
//    public OrarOra build OrarOra(final OrarOraDTO orarOraDTO) {
//        OrarOra orarOra = new OrarOra();
//        orarOra.setOrarId(orarOraDTO.getOrarId());
//        orarOra.setOraId(orarOraDTO.getOraId());
//        return orarOra;
//    }
//
//    public OrarOra add(final OrarOra orarOra) {
//        return orarOraRepository.save(orarOra);
//    }
//}
