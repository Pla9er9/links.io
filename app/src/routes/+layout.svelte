<script>
	import Dialog from "$components/Dialog.svelte";
	import Navbar from "$components/Navbar.svelte";
	import Toast from "$components/Toast.svelte";
	import { userStore } from "$lib/userStore";

	export let data;
	userStore.update(u => {
		u.token = data.token
		u.username = data.username
		return u
	})
</script>

<div class="max-w-[1200px] mx-auto">
	<Navbar />
	<Dialog />
	<Toast />
	<slot></slot>
</div>
