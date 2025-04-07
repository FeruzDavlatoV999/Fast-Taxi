package uz.mobile.joybox.data.remote.mapper

import uz.mobile.joybox.data.remote.dto.CommentDTO
import uz.mobile.joybox.data.util.formatDate
import uz.mobile.joybox.domain.model.Comment


fun CommentDTO.toComment(): Comment = Comment(
    id = id ?: -1,
    comment = body.orEmpty(),
    userId = userLessons?.user?.id ?: -1,
    userName = userLessons?.user?.profile?.firstName.orEmpty(),
    userPhoto = userLessons?.user?.profile?.avatar.orEmpty(),
    date = formatDate(createdAt.orEmpty()),
    rating = rate ?: 0f,
)


fun List<CommentDTO>.toComments(): List<Comment> = this.map { it.toComment() }