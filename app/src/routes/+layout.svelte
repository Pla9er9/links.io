<script>
	import Dialog from "$components/Dialog.svelte";
	import Navbar from "$components/Navbar.svelte";
</script>

<div class="max-w-[1200px] mx-auto">
	<Navbar />
	<Dialog />
	<slot></slot>
</div>
