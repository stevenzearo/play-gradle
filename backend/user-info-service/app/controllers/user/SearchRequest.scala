package controllers.user

/**
 * @author steve
 */
class SearchRequest {
    var name: String = _

    def this(name: String) = {
        this()
        this.name = name
    }
}

object SearchRequest {
    def apply(): SearchRequest = new SearchRequest()
    def apply(name: String): SearchRequest = new SearchRequest(name)

    def unapply(request: SearchRequest): Option[(String)] = {
        if (request == null) return None
        Some(request.name)
    }
}
