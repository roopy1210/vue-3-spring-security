<template>
  <form @submit.prevent="handleSubmit">
    <h3>사용자등록</h3>
    <input type="text" required placeholder="display name" v-model="displayName">
    <input type="email" required placeholder="Email" v-model="email">
    <input type="password" required placeholder="Password" v-model="password">
    <div v-if="error" class="error">{{ error }}</div>
    <button v-if="!isPending">사용자등록</button>
    <button v-if="isPending" disable>Loading</button>
  </form>
</template>

<script>
import useSignup from '@/composables/useSignup'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

export default {
    setup() {
        const { error, signup, isPending, statusCode } = useSignup()
        const router = useRouter()

        const email = ref('')
        const password = ref('')
        const displayName = ref('')

        const handleSubmit = async () => {
            await signup(email.value, password.value, displayName.value)
            if (!error.value) {
              router.push({ name: 'Login' })
            }
        }

        return { email, password, displayName, isPending, error, handleSubmit }
    }
}
</script>

<style>

</style>