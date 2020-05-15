package controllers.user

import domain.UserInfo

/**
 * @author steve
 */
class SearchResponse {
    var userInfos: List[UserInfo] = _
    def this(userInfos: List[UserInfo]) {
        this()
        this.userInfos = userInfos
    }
}

object SearchResponse {
    def apply(): SearchResponse = new SearchResponse()

    def apply(userInfos: List[Tuple3[Long, String, Int]]): SearchResponse = {
        val response = new SearchResponse()
        response.userInfos = userInfos.map(userInfo => new UserInfo(userInfo._1, userInfo._2, userInfo._3))
        response
    }

    def unapply(response: SearchResponse): Option[List[Tuple3[Long, String, Int]]] = {
        if (response == null) return None
        if (response.userInfos.isEmpty) return Some(List.apply())
        val values = response.userInfos.map(userInfo => (userInfo.id, userInfo.name, userInfo.age))
        Some(values)
    }
}