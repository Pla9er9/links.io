<script lang="ts">
	import { CogOutline, LinkOutline } from 'flowbite-svelte-icons';
	import '../app.css';
	import { Navbar, NavBrand, Button, Avatar } from 'flowbite-svelte';
	import { updateDialog } from '$lib/dialogStore';
	import { userStore, type userData } from '$lib/userStore';

	let user: userData;
	userStore.subscribe((u) => {
		user = u;
	});
</script>

<Navbar rounded class="mx-auto px-4 py-4" style="background: transparent">
	<NavBrand href="/">
		<LinkOutline size="lg" color="pink" />
		<i class="ml-2 text-2xl font-semibold text-white">Links.io</i>
	</NavBrand>
	{#if user.username}
		<div class="row space-x-4">
			<a href="/settings">
				<Button outline class="w-12 rounded-3xl">
					<CogOutline color="#fff" size="md" />
				</Button>
			</a>
			<a href="/{user.username}">
				<Button outline class="rounded-3xl">
					<span class="text-white">{user.username}</span>
				</Button>
			</a>
		</div>
	{:else}
		<Button pill outline on:click={() => updateDialog('login')}>Sign In</Button>
	{/if}
</Navbar>
