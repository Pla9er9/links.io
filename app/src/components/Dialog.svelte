<script lang="ts">
    import { dialogStroe, updateDialog, type dialog } from "$lib/dialogStore";
	import LoginDialog from "./LoginDialog.svelte";
	import RegistrationDialog from "./RegistrationDialog.svelte";

    let currentDialog: dialog = null
    
    dialogStroe.subscribe((d) => {
        currentDialog = d
    })
</script>

{#if currentDialog} 
    <button class="darkScreen dialog" on:click={() => updateDialog(null)}></button>
{/if}
{#if currentDialog === "registration"}
    <RegistrationDialog />
{:else if currentDialog === "login"}
    <LoginDialog />
{/if}

<style>
    .darkScreen {
        width: 100vw;
        height: 100vh;
        animation-name: transparentToDark;
        animation-duration: 0.5s;
        animation-fill-mode: both;
        cursor: default;
    }
    
    @keyframes transparentToDark {
        0% {
            background-color: transparent;
        }
        100% {
            background-color: #000000c5;
        }
    }
</style>