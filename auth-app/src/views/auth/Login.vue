<template>
  <form @submit.prevent="handleSubmit">
      <h3>로그인</h3>
      <input type="email" placeholder="Email" v-model="username">
      <input type="password" placeholder="Password" v-model="password">
      <div v-if="error" class="error">{{ error }}</div> 
      <button v-if="!isPending">로그인</button>
      <button v-if="isPending" disable>Loading</button>
  </form>
</template>

<script>
import useLogin from '@/composables/useLogin'
import { useStore } from 'vuex'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
    setup() {
        const{ error, login, isPending } = useLogin()
        const store = new useStore()
        const router = useRouter()

        const username = ref('')
        const password = ref('')

        const handleSubmit = async () => {
            const res = await login(username.value, password.value)

            if (store.state.auth.status.loggedIn) {
                router.push({ name: 'Profile' })
            }
        }

        return { username, password, handleSubmit, error, isPending }
    }
}
</script>

<style>

</style>