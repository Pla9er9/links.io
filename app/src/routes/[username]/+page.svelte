<script lang="ts">
	import Link from '$components/Link.svelte';
	import { Avatar } from 'flowbite-svelte';

	export let data;

	const profile = data.profile;
</script>

<div class="column mt-16">
	<Avatar size="xl">
		<span class="text-5xl">{profile.username.slice(0, 2)}</span>
	</Avatar>
	<h1 class="mt-8 text-5xl font-black text-pink-100">{profile.username}</h1>
	<p class="text-medium mb-12 mt-4 text-gray-400">{profile.description}</p>
	{#each profile.links as link}
		<Link url="https://{link.url}" text={link.text} />
	{/each}
</div>
