package com.example.petdateapp.data.database

import com.example.petdateapp.data.entity.MascotaEntity
import com.example.petdateapp.data.entity.UsuarioEntity

// Capa que centraliza las operaciones y orquesta DAO(s)
class Repositorio(
    private val db: AppDatabase
) {
    private val usuarioDao = db.usuarioDao()
    private val mascotaDao = db.mascotaDao()

    // -------- Usuarios --------
    suspend fun registrarUsuario(usuario: UsuarioEntity) = usuarioDao.insert(usuario)
    suspend fun login(correo: String, contrasena: String) = usuarioDao.login(correo, contrasena)
    suspend fun buscarUsuario(correo: String) = usuarioDao.findByCorreo(correo)

    // -------- Mascotas --------
    suspend fun agregarMascota(mascota: MascotaEntity) = mascotaDao.insert(mascota)
    suspend fun mascotasDe(correoDueno: String) = mascotaDao.getByDueno(correoDueno)
    suspend fun borrarMascotasDe(correoDueno: String) = mascotaDao.deleteByDueno(correoDueno)
}
