<script lang="ts">
	import { toastStore, type toast } from '$lib/toastStore';
	import { Toast } from 'flowbite-svelte';
	import { CloseCircleSolid } from 'flowbite-svelte-icons';
	import { slide } from 'svelte/transition';

	let toasts: toast[];
	toastStore.subscribe((t) => {
		toasts = t;
	});
</script>

{#each toasts as t}
	<Toast transition={slide} position="bottom-right" color="red">
		<svelte:fragment slot="icon">
			<CloseCircleSolid class="h-5 w-5" />
			<span class="sr-only">Error icon</span>
		</svelte:fragment>
		{#if t.title}
			<h1 class="font-medium text-white">{t.title}</h1>
		{/if}
		{t.text}
	</Toast>
{/each}
