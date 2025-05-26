<template>
  <div class="comment-section">
    <h3 class="comment-title">
      댓글 <span class="comment-count">{{ commentCount }}</span>
    </h3>

    <div class="comment-form">
      <div class="comment-input-container">
        <textarea
          v-model="commentText"
          class="comment-input"
          placeholder="댓글을 작성해주세요"
          rows="3"
        ></textarea>
        <div class="comment-actions">
          <button class="comment-submit" @click="submitComment" :disabled="!commentText.trim()">
            댓글 작성
          </button>
        </div>
      </div>
    </div>

    <div class="comment-list">
      <div v-if="comments.length === 0" class="no-comments">
        <p>첫 번째 댓글을 작성해보세요!</p>
      </div>

      <div v-else>
        <div v-for="(comment, index) in comments" :key="index" class="comment-item">
          <div class="comment-header">
            <div class="comment-author">
              <!-- <div class="author-avatar">
                <img
                  :src="comment.author.avatar || 'https://via.placeholder.com/30'"
                  :alt="comment.author.name"
                />
              </div> -->
              <div class="author-name">{{ comment.authorName }}</div>
            </div>
            <div class="comment-date">{{ formatDate(comment.createdAt) }}</div>
          </div>

          <div class="comment-content">
            {{ comment.content }}
          </div>

          <div class="comment-footer">
            <button class="reply-btn" @click="toggleReplyForm(index)">
              <i class="bi bi-reply"></i> 답글
            </button>
            <button
              v-if="comment.author"
              class="delete-btn"
              @click="deleteComment(comment.id)"
            >
              <i class="bi bi-trash"></i> 삭제
            </button>
          </div>

          <div v-if="replyFormIndex === index" class="reply-form">
            <div class="reply-input-container">
              <textarea
                v-model="replyText"
                class="reply-input"
                placeholder="답글을 작성해주세요"
                rows="2"
              ></textarea>
              <div class="reply-actions">
                <button class="reply-cancel" @click="cancelReply">취소</button>
                <button
                  class="reply-submit"
                  @click="submitReply(comment.id)"
                  :disabled="!replyText.trim()"
                >
                  답글 작성
                </button>
              </div>
            </div>
          </div>

          <div v-if="comment.replies && comment.replies.length > 0" class="reply-list">
            <div
              v-for="(reply, replyIndex) in comment.replies"
              :key="`reply-${index}-${replyIndex}`"
              class="reply-item"
            >
              <div class="reply-header">
                <div class="reply-author">
                  <!-- <div class="author-avatar">
                    <img
                      :src="reply.author.avatar || 'https://via.placeholder.com/30'"
                      :alt="reply.author.name"
                    />
                  </div> -->
                  <div class="author-name">{{ reply.authorName }}</div>
                </div>
                <div class="reply-date">{{ formatDate(reply.createdAt) }}</div>
              </div>

              <div class="reply-content">
                {{ reply.content }}
              </div>

              <div class="reply-footer">
                <button
                  v-if="reply.author"
                  class="delete-btn"
                  @click="deleteReply(comment.id, reply.id)"
                >
                  <i class="bi bi-trash"></i> 삭제
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import api from '@/api/axios'

const props = defineProps({
  tripostId: {
    type: String,
    required: true,
  },
  commentCount: {
    type: Number,
  },
  comments: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['add-comment', 'add-reply', 'delete-comment', 'delete-reply'])

const commentText = ref('')
const replyText = ref('')
const replyFormIndex = ref(null)
const currentUser = ref({
  id: 1, // 실제 구현 시 로그인 사용자 정보 사용
  name: '현재 사용자',
  avatar: 'https://via.placeholder.com/30?text=User',
})

async function submitComment() {
  if (!commentText.value.trim()) return

  const newComment = {
    id: "",
    authorName: "",
    content: commentText.value,
    createdAt: null,
    author: false,
  }

  try {
    const response = await api.post(`/api/v1/triposts/${props.tripostId}/comments`, {
      tripostId: props.tripostId,
      content: newComment.content
    })

    Object.assign(newComment, response.data);
    emit('add-comment', newComment)
    commentText.value = ''
  } catch (error) {
    alert("댓글 저장 실패")
    console.log(error)
  }

}

function toggleReplyForm(index) {
  replyFormIndex.value = replyFormIndex.value === index ? null : index
  replyText.value = ''
}

function cancelReply() {
  replyFormIndex.value = null
  replyText.value = ''
}

async function submitReply(commentId) {
  if (!replyText.value.trim()) return

  console.log(commentId)
  const newReply = {
    id: "",
    parentId: commentId,
    authorName: "",
    content: replyText.value,
    createdAt: null,
    author: false,
  }

  try {
    const response = await api.post(`/api/v1/triposts/${props.tripostId}/comments`, {
      parentId: newReply.parentId,
      tripostId: props.tripostId,
      content: newReply.content
    })

    Object.assign(newReply, response.data);
    emit('add-reply', { commentId, reply: newReply })
    replyText.value = ''
    replyFormIndex.value = null
  } catch (error) {
    alert("댓글 저장 실패")
    console.log(error)
  }
}

async function deleteComment(commentId) {
  if (confirm('댓글을 삭제하시겠습니까?')) {
    try {
      const response = await api.delete(`/api/v1/triposts/${props.tripostId}/comments/${commentId}`);
      console.log(response);
      emit('delete-comment', commentId)
    } catch (error) {
      alert("댓글 삭제 실패")
      console.log(error)
    }
  }
}

async function deleteReply(commentId, replyId) {
  if (confirm('답글을 삭제하시겠습니까?')) {
    try {
      const response = await api.delete(`/api/v1/triposts/${props.tripostId}/comments/${replyId}`);
      console.log(response);
      emit('delete-reply', { commentId, replyId })
    } catch (error) {
      alert("댓글 삭제 실패")
      console.log(error)
    }
  }
}

function formatDate(date) {
  if (!date) return ''

  const d = new Date(date)
  const now = new Date()
  const diff = Math.floor((now - d) / 1000) // 초 단위 차이

  if (diff < 60) {
    return '방금 전'
  } else if (diff < 3600) {
    return `${Math.floor(diff / 60)}분 전`
  } else if (diff < 86400) {
    return `${Math.floor(diff / 3600)}시간 전`
  } else if (diff < 604800) {
    return `${Math.floor(diff / 86400)}일 전`
  } else {
    return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}.${String(d.getDate()).padStart(2, '0')}`
  }
}
</script>

<style scoped>
.comment-section {
  margin: 30px 0;
  border-top: 1px solid #e9ecef;
  padding-top: 30px;
}

.comment-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #333;
  display: flex;
  align-items: center;
}

.comment-count {
  margin-left: 5px;
  color: #fd7e14;
}

.comment-form {
  margin-bottom: 30px;
}

.comment-input-container {
  border: 1px solid #ced4da;
  border-radius: 8px;
  overflow: hidden;
}

.comment-input {
  width: 100%;
  padding: 15px;
  border: none;
  resize: none;
  font-size: 16px;
  font-family: inherit;
}

.comment-input:focus {
  outline: none;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  padding: 10px;
  background-color: #f8f9fa;
}

.comment-submit {
  padding: 8px 15px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.comment-submit:hover {
  background-color: #e8590c;
}

.comment-submit:disabled {
  background-color: #adb5bd;
  cursor: not-allowed;
}

.no-comments {
  text-align: center;
  padding: 30px 0;
  color: #6c757d;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  padding: 15px;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  background-color: white;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.comment-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.author-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  overflow: hidden;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: #495057;
}

.comment-date {
  color: #6c757d;
  font-size: 12px;
}

.comment-content {
  margin-bottom: 15px;
  line-height: 1.5;
}

.comment-footer {
  display: flex;
  gap: 15px;
}

.reply-btn,
.delete-btn {
  background: none;
  border: none;
  color: #6c757d;
  font-size: 14px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.reply-btn:hover {
  color: #fd7e14;
}

.delete-btn:hover {
  color: #dc3545;
}

.reply-form {
  margin-top: 15px;
  margin-bottom: 15px;
}

.reply-input-container {
  border: 1px solid #ced4da;
  border-radius: 8px;
  overflow: hidden;
}

.reply-input {
  width: 100%;
  padding: 10px;
  border: none;
  resize: none;
  font-size: 14px;
  font-family: inherit;
}

.reply-input:focus {
  outline: none;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 8px;
  background-color: #f8f9fa;
}

.reply-cancel {
  padding: 6px 12px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.reply-submit {
  padding: 6px 12px;
  background-color: #fd7e14;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.reply-cancel:hover {
  background-color: #5a6268;
}

.reply-submit:hover {
  background-color: #e8590c;
}

.reply-submit:disabled {
  background-color: #adb5bd;
  cursor: not-allowed;
}

.reply-list {
  margin-top: 15px;
  padding-left: 20px;
  border-left: 2px solid #e9ecef;
}

.reply-item {
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.reply-author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.reply-date {
  color: #6c757d;
  font-size: 12px;
}

.reply-content {
  margin-bottom: 10px;
  line-height: 1.5;
  font-size: 14px;
}

.reply-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
