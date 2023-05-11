package com.cmc12th.domain.model

enum class ReviewReportType(val id: Int, val reason: String) {
    SPAM(1, "스팸 홍보 / 도배글이에요"),
    INAPPROPRIATE(2, "부적절한 사진이에요"),
    HARMFUL(3, "청소년에게 유해한 내용이에요"),
    ANATHEMA(4, "욕설/ 혐오/ 차별적 표현을 담고있어요"),
    FALSE_INFORMATION(5, "거짓 정보를 담고 있어요"),
    ETC(6, "기타"),
}