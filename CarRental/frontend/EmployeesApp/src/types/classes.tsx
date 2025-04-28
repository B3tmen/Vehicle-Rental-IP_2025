export class CustomError extends Error {
    status: number;
    isAxiosError: boolean;
  
    constructor(status: number, message: string, isAxiosError: boolean) {
      super(message);
      this.status = status;
      this.isAxiosError = isAxiosError;
      this.name = 'CustomError';
      
      // Maintains proper stack trace for where our error was thrown
    //   if (Error.captureStackTrace) {
    //     Error.captureStackTrace(this, CustomError);
    //   }
    }
  }