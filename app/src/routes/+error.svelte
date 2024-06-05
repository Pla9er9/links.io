<script>
	import { goto } from '$app/navigation';
	import { Button } from 'flowbite-svelte';
    import { HomeOutline, RefreshOutline } from 'flowbite-svelte-icons';

	function refresh() {
		window.location.reload();
	}

	async function goHome() {
		await goto('/');
	}
</script>

<div class="column lg:absolute mt-12 inset-0 lg:m-auto h-max w-screen max-w-[1000px]">
	<h1 class="text-center text-5xl px-4 md:text-7xl lg:text-9xl font-bold text-[#fdddc4]">
		Unknown error occurred
	</h1>
	<div class="row mt-16 space-x-4 flex-wrap justify-center">
		<Button size="xl" color="primary" on:click={goHome}>
            <HomeOutline />
            <span class="ml-2">Go home</span>
        </Button>
		<Button size="xl" outline color="alternative" on:click={refresh}>
            <RefreshOutline />
            <span class="ml-2">Refresh</span>
        </Button>
	</div>
</div>
