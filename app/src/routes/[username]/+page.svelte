<script lang="ts">
    export let data

    const profile = data.profile)dsadasdas
</script>

<div class="column">
    <h1>{data.profile.username}</h1>
    <p>{description}</p>
    {#each data.profile.links as link}
        <a href="{link}">{link}</a>
    {/each}
</div>