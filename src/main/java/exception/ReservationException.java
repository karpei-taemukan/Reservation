package exception;


import lombok.*;

@Getter
public class ReservationException extends RuntimeException{
   private ErrorCode errorCode;
   public ReservationException(ErrorCode errorCode){
       super(errorCode.getDescription());
       this.errorCode = errorCode;
   }
}
