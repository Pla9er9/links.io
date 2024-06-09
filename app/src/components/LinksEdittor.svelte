<script lang="ts">
	import { Button, Card, Input } from 'flowbite-svelte';
	import { LinkOutline } from 'flowbite-svelte-icons';

	export let links: { text: string; url: string }[];

	function addLink() {
		links = [...links, { text: '', url: '' }];
	}

	function deleteAll() {
		links = [];
	}
</script>

{#each links as link}
	<div class="row w-full space-x-2" style="margin: 15px 0;">
		<Button color="alternative" checked style="cursor: default" class="w-4">
			<LinkOutline />
		</Button>
		<Input placeholder="Name" bind:value={link.text} />
		<Input placeholder="Url" bind:value={link.url} />
	</div>
{/each}
<div class="row mb-4 space-x-2">
	<Button outline on:click={addLink}>Add link</Button>
	{#if links.length > 0}
		<Button color="red" outline on:click={deleteAll}>Delete all</Button>
	{/if}
</div>
