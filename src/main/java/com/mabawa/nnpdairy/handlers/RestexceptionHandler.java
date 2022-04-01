package com.mabawa.nnpdairy.handlers;

import com.mabawa.nnpdairy.models.AdviceResponse;
import com.mabawa.nnpdairy.models.Response;
import org.jboss.logging.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class RestexceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = Logger.getLogger(RestexceptionHandler.class);

        /**
         * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
         *
         * @param ex      MissingServletRequestParameterException
         * @param headers HttpHeaders
         * @param status  HttpStatus
         * @param request WebRequest
         * @return the ApiError object
         */
        @Override
        protected ResponseEntity<Object> handleMissingServletRequestParameter(
                MissingServletRequestParameterException ex, HttpHeaders headers,
                HttpStatus status, WebRequest request) {
            String error = ex.getParameterName() + " parameter is missing";
            log.error(error);
            return buildResponseEntity(new AdviceResponse(BAD_REQUEST));
        }


        /**
         * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
         *
         * @param ex      HttpMediaTypeNotSupportedException
         * @param headers HttpHeaders
         * @param status  HttpStatus
         * @param request WebRequest
         * @return the ApiError object
         */
        @Override
        protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
                HttpMediaTypeNotSupportedException ex,
                HttpHeaders headers,
                HttpStatus status,
                WebRequest request) {
            StringBuilder builder = new StringBuilder();
            builder.append(ex.getContentType());
            builder.append(" media type is not supported. Supported media types are ");
            ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
            log.error(" media type is not supported");
            return buildResponseEntity(new AdviceResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
        }

//        /**
//         * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
//         *
//         * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
//         * @param headers HttpHeaders
//         * @param status  HttpStatus
//         * @param request WebRequest
//         * @return the ApiError object
//         */
//        @Override
//        protected ResponseEntity<Object> handleMethodArgumentNotValid(
//                MethodArgumentNotValidException ex,
//                HttpHeaders headers,
//                HttpStatus status,
//                WebRequest request) {
//            AdviceResponse apiError = new AdviceResponse(BAD_REQUEST);
//            apiError.setMessage("Validation error");
//            apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
//            apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
//            log.error("Validation error");
//            return buildResponseEntity(apiError);
//        }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
//    @ExceptionHandler(ConstraintViolationException.class)
//        protected ResponseEntity<Object> handleConstraintViolation(
//                ConstraintViolationException ex) {
//            AdviceResponse apiError = new AdviceResponse(BAD_REQUEST);
//            apiError.setMessage("Validation error");
//            apiError.addValidationErrors(ex.getConstraintViolations());
//            log.error("validation error");
//            return buildResponseEntity(apiError);
//        }



        /**
         * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
         *
         * @param ex      HttpMessageNotReadableException
         * @param headers HttpHeaders
         * @param status  HttpStatus
         * @param request WebRequest
         * @return the ApiError object
         */
        @Override
        protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) request;
//            log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
            String error = "Malformed JSON request";
            log.error(error);
            return buildResponseEntity(new AdviceResponse(HttpStatus.BAD_REQUEST, error, ex));
        }

        /**
         * Handle HttpMessageNotWritableException.
         *
         * @param ex      HttpMessageNotWritableException
         * @param headers HttpHeaders
         * @param status  HttpStatus
         * @param request WebRequest
         * @return the ApiError object
         */
        @Override
        protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            String error = "Error writing JSON output";
            log.error(error);
            return buildResponseEntity(new AdviceResponse(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
        }

        /**
         * Handle NoHandlerFoundException.
         *
         * @param ex
         * @param headers
         * @param status
         * @param request
         * @return
         */
        @Override
        protected ResponseEntity<Object> handleNoHandlerFoundException(
                NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
            AdviceResponse apiError = new AdviceResponse(BAD_REQUEST);
            apiError.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
            apiError.setDebugMessage(ex.getMessage());
            log.error("missing handler");
            return buildResponseEntity(apiError);
        }

        /**
         * Handle javax.persistence.EntityNotFoundException
         */
//        @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
//        protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
//            return buildResponseEntity(new AdviceResponse(NOT_FOUND, ex));
//        }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex      the DataIntegrityViolationException
     * @param request the request
     * @return the ApiError object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
        protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                      WebRequest request) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                log.error("Database error");
                return buildResponseEntity(new AdviceResponse(HttpStatus.CONFLICT, "Database error", ex.getCause()));
            }
            log.error("data integrity error "+ex.getLocalizedMessage());
            return buildResponseEntity(new AdviceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex));
        }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex      the Exception
     * @param request the request
     * @return the ApiError object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                          WebRequest request) {
            AdviceResponse apiError = new AdviceResponse(BAD_REQUEST);
            apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
            apiError.setDebugMessage(ex.getMessage());
            log.error("parameter conversion error");
            return buildResponseEntity(apiError);
        }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handlePermissionDeniedException(AccessDeniedException ex,
                                                                      WebRequest request) {
        AdviceResponse apiError = new AdviceResponse(HttpStatus.FORBIDDEN);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        log.error("permission denied error");
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(AdviceResponse adviceResponse) {
        log.error("returning Api error "+adviceResponse.getMessage());
        return new ResponseEntity<>(adviceResponse, adviceResponse.getStatus());
    }

     @ExceptionHandler(EntityNotFoundException.class)
     @ResponseStatus(BAD_REQUEST)
     public ResponseEntity<Object> handleEntityNotFoundException(
             EntityNotFoundException exception,
             WebRequest request
     ){
         log.error("Unknown error occurred", exception);
         AdviceResponse apiError = new AdviceResponse(BAD_REQUEST);
         apiError.setMessage(exception.getMessage());
         return buildResponseEntity(apiError);
     }
    /**
     *handle unknown server error
     * @param exception
     * @param request
     * @return {@link ResponseEntity }
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request){
        log.error("Unknown error occurred", exception);
        AdviceResponse apiError = new AdviceResponse(INTERNAL_SERVER_ERROR);
        apiError.setMessage("An Error occurred in the server side. \n"+exception.getMessage());
        return buildResponseEntity(apiError);
    }
}
