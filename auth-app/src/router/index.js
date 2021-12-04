import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/auth/Login.vue'
import Signup from '../views/auth/Signup.vue'
import Profile from '../views/Profile.vue'
import Admin from '../views/Admin.vue'
import User from '../views/User.vue'
import Forbidden from '../views/Forbidden.vue'
import PageNotFound from '../views/PageNotFound.vue'

import store from '../store'

const requireAuth = (to, from, next) => {
  let isLoggedIn = store.state.auth.status.loggedIn

  if (!isLoggedIn) {
    next({ name: 'Login' })
  } else {
    next()
  }
}

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    beforeEnter: requireAuth
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    beforeEnter: requireAuth
  },
  {
    path: '/forbidden',
    name: 'Forbidden',
    component: Forbidden
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    beforeEnter: requireAuth
  },
  {
    path: '/user',
    name: 'User',
    component: User,
    beforeEnter: requireAuth
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  },
  {
    path: '/404',
    component: PageNotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
