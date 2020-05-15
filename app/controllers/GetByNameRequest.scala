package controllers

/**
 * @author steve
 */
class GetByNameRequest {
    var name: String = _

    def this(name: String) {
        this()
        this.name = name
    }
}

object GetByNameRequest {
    def apply(): GetByNameRequest = new GetByNameRequest()
    def apply(name: String): GetByNameRequest = new GetByNameRequest(name)

    def unapply(request: GetByNameRequest): Option[(String)] = {
        if (request == null) return None
        Some(request.name)
    }
}
