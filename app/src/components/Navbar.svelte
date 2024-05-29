<script>
	import { LinkOutline } from 'flowbite-svelte-icons';
	import '../app.css';
	import { Navbar, NavBrand, Button } from 'flowbite-svelte';
	import { updateDialog } from '$lib/dialogStore';
</script>

<Navbar rounded class="mx-auto px-4 py-4" style="background: transparent">
	<NavBrand href="/">
		<LinkOutline size="lg" color="pink" />
		<i class="ml-2 text-2xl font-semibold text-white">Links.io</i>
	</NavBrand>
	<Button pill outline on:click={() => updateDialog('registration')}>Sign In</Button>
</Navbar>
