<template>
  <div class="navbar">
      <nav>
          <img src="@/assets/roopy.png">
          <h1><router-link :to="{name: 'Home' }">Roopy</router-link></h1>          

          <div class="links">
            <div v-if="isLogin">
              <button @click="handleClick">로그아웃</button>    
            </div>
            <div v-else>
              <router-link class="btn" :to="{ name: 'Signup' }">사용자등록</router-link>
              <router-link class="btn" :to="{ name: 'Login' }">로그인</router-link>
            </div>
          </div>
      </nav>
  </div>
</template>

<script>
import useLogout from "@/composables/useLogout"
import getUser from '@/composables/getUser'

import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { computed } from 'vue'

export default {
  setup() {
    const { logout } = useLogout()
    const router = useRouter()
    const store = new useStore()

    const isLogin = computed(() => store.state.auth.status.loggedIn)
    const{ user } = getUser()

    const handleClick = async () => {
      await logout()

      router.push({name: 'Login'})
    }

    return { handleClick, isLogin, user }
  }
}
</script>

<style scoped>
  .navbar {
    padding: 16px 10px;
    margin-bottom: 60px;
    background: white;
  }
  nav {
    display: flex;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
  }
  nav img {
    max-height: 60px;
  }
  nav h1 {
    margin-left: 20px;
  }
  nav .links {
    margin-left: auto;
  }
  nav .links a, button {
    margin-left: 16px;
    font-size: 14px;
  }
</style>