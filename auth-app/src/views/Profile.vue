<template>
  <div class="content">
    <p>
      <strong>AccessToken:</strong>
      {{ user.accessToken.substring(0, 20) }} ... {{ user.accessToken.substr(user.accessToken.length - 20) }}
    </p>
    <p>
      <strong>Usernmae:</strong>
      {{ user.username }}
    </p>     
    <p>
      <strong>Nickname:</strong>
      {{ user.displayName }}
    </p>         
    <strong>Authorities:</strong>
    <ul>
      <li v-for="role in user.roles" :key="role">{{role}}</li>
    </ul>
  </div>
</template>

<script>
import getUser from '@/composables/getUser'

export default {
  setup() {
    const{ user } = getUser()

    return { user }
  }
}
</script>

<style>
content {
  width: 1200px;
}

ul {
  line-height: 1.5em;
  margin: 1px 0 15px;
  padding: 0;
}

li {
  list-style: none;
  position: relative;
  padding: 0 0 0 20px;
  font-size: 0.9em;
}

</style>