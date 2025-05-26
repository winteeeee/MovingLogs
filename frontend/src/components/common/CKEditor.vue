<template>
  <div class="ckeditor-container">
    <div ref="editorContainer"></div>
  </div>
</template>

<script setup>
import { shallowRef, markRaw, ref, watch, onMounted, onBeforeUnmount, defineProps, defineEmits } from 'vue'

const props = defineProps({
  value: {
    type: String,
    default: '',
  },
  config: {
    type: Object,
    default: () => ({}),
  },
})

const emit = defineEmits(['input', 'change', 'ready'])

const editor = shallowRef(null)
const editorData = ref(props.value)
const editorContainer = ref(null)

watch(
  () => props.value,
  (newValue) => {
    editor.value.setData(newValue)
  },
)

onMounted(() => {
  initEditor()
})

onBeforeUnmount(() => {
  if (editor.value) {
    editor.value
      .destroy()
      .then(() => {
        editor.value = null
      })
      .catch((error) => {
        console.error('CKEditor 제거 중 오류 발생:', error)
      })
  }
})

function initEditor() {
  // CKEditor 스크립트 동적 로드
  if (window.ClassicEditor) {
    createEditor()
  } else {
    const script = document.createElement('script')
    script.src = 'https://cdn.ckeditor.com/ckeditor5/38.0.1/classic/ckeditor.js'
    script.async = true
    script.onload = () => {
      createEditor()
    }
    document.head.appendChild(script)
  }
}

function createEditor() {
  const config = {
    ...props.config,
    placeholder: '내용을 입력하세요...',
    toolbar: {
      items: [
        'heading',
        '|',
        'bold',
        'italic',
        'link',
        'bulletedList',
        'numberedList',
        '|',
        'outdent',
        'indent',
        '|',
        'blockQuote',
        'insertTable',
        'undo',
        'redo',
      ],
    },
    language: 'ko',
    image: {
      toolbar: ['imageTextAlternative', 'imageStyle:full', 'imageStyle:side'],
    },
    table: {
      contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells'],
    },
  }

  window.ClassicEditor.create(editorContainer.value, config)
    .then((editorInstance) => {
      editor.value = markRaw(editorInstance)

      // 초기 데이터 설정
      if (props.value) {
        editor.value.setData(props.value)
      }

      // 변경 이벤트 리스너
      editor.value.model.document.on('change:data', () => {
        const data = editor.value.getData()
        emit('input', data)
        emit('change', data)
      })

      // 에디터 준비 완료 이벤트
      emit('ready', editor.value)
    })
    .catch((error) => {
      console.error('CKEditor 초기화 중 오류 발생:', error)
    })
}
</script>

<style scoped>
.ckeditor-container {
  border: 1px solid #ced4da;
  border-radius: 4px;
}

:deep(.ck-editor__editable) {
  min-height: 500px;
  max-height: 500px;
  overflow-y: auto;
}

:deep(.ck-editor__editable_inline) {
  padding: 0 15px;
}

:deep(.ck.ck-toolbar) {
  border-bottom: 1px solid #ced4da;
  background: #f8f9fa;
}

:deep(.ck.ck-editor__main) {
  background: white;
}
</style>
