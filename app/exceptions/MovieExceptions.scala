package exceptions

class InvalidQueryParamException(message: String) extends Exception
class GenericFailureCalling(message: String) extends Exception
class FailureDecodingJson(message: String) extends Exception