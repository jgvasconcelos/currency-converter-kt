package br.com.jose.currencyconverter.exception.handler

import br.com.jose.currencyconverter.exception.ResourceAlreadyExistsException
import br.com.jose.currencyconverter.exception.ResourceNotFoundException
import br.com.jose.currencyconverter.exception.TransactionBadRequestException
import br.com.jose.currencyconverter.exception.error.MethodArgumentViolation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvisor {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun resourceNotFoundExceptionHandler(resourceNotFoundException: ResourceNotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.message)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidExceptionHandler(methodArgumentNotValidException: MethodArgumentNotValidException): ResponseEntity<Collection<MethodArgumentViolation>> {
        val violations: MutableCollection<MethodArgumentViolation> = ArrayList()
        for (fieldError in methodArgumentNotValidException.bindingResult.fieldErrors) {
            violations.add(MethodArgumentViolation(fieldError.field, fieldError.defaultMessage!!))
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(violations)
    }

    @ExceptionHandler(ResourceAlreadyExistsException::class)
    fun resourceAlreadyRegisteredExceptionHandler(resourceAlreadyExistsException: ResourceAlreadyExistsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resourceAlreadyExistsException.message)
    }

    @ExceptionHandler(TransactionBadRequestException::class)
    fun transactionBadRequestExceptionHandler(transactionBadRequestException: TransactionBadRequestException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transactionBadRequestException.message)
    }
}