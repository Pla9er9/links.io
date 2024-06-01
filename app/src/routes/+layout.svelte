<script>
	import Dialog from "$components/Dialog.svelte";
	import Navbar from "$components/Navbar.svelte";
	import Toast from "$components/Toast.svelte";
</script>

<div class="max-w-[1200px] mx-auto">
	<Navbar />
	<Dialog />
	<Toast />
	<slot></slot>
</div>
