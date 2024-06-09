<script lang="ts">
	import { toastStore, type toast } from '$lib/toastStore';
	import { Toast } from 'flowbite-svelte';
	import { CheckCircleSolid, CloseCircleSolid } from 'flowbite-svelte-icons';
	import { slide } from 'svelte/transition';

	let toasts: toast[];
	toastStore.subscribe((t) => {
		toasts = t;
	});

	function getColorOfVariant(variant: string | null): 'red' | 'green' | undefined {
		if (!variant) {
			return 'red';
		}

		switch (variant) {
			case 'danger':
				return 'red';
			case 'succes':
				return 'green';
			default:
				return undefined;
		}
	}
</script>

{#each toasts as t}
	<Toast transition={slide} position="bottom-right" color={getColorOfVariant(t.variant)}>
		<svelte:fragment slot="icon">
			{#if t.variant === 'danger'}
				<CloseCircleSolid class="h-5 w-5" />
				<span class="sr-only">Error icon</span>
			{:else if t.variant === 'succes'}
				<CheckCircleSolid class="h-5 w-5" />
				<span class="sr-only">Succes icon</span>
			{/if}
		</svelte:fragment>
		{#if t.title}
			<h1 class="font-medium text-white">{t.title}</h1>
		{/if}
		{t.text}
	</Toast>
{/each}
