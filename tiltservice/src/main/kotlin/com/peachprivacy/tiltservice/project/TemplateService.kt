package com.peachprivacy.tiltservice.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class TemplateService @Autowired constructor(
    private val templateRepository: TemplateRepository
) {
    fun get(id: UUID): Template? {
        return templateRepository.findByIdOrNull(id)
    }

    fun update(template: Template): Template {
        return templateRepository.save(template)
    }

    fun create(template: Template): UUID {
        return templateRepository.save(template).id!!
    }

    fun delete(id: UUID) {
        return templateRepository.deleteById(id)
    }
}