<template>
    <div class="wrapper">
        <h2>{{ msg }}</h2>
    </div>
</template>

<script>
import UserService from '../services/user.service'
import { ref } from 'vue'

export default {
    name: 'Admin',
    setup() {
        let msg = ref('')

        UserService.getAuthorityTest('admin').then(
            (response) => {
                if (response.data) {
                    msg.value = response.data.msg
                }

                return response
            },
            (error) => {
                return Promise.reject(error);
            }
        );

        return { msg }
    }
}
</script>

<style>
  .wrapper {
    display: flex;
    justify-content: center;
  }

  h2 {
    color: blueviolet;
  }
</style>