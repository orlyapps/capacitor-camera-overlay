import { WebPlugin } from "@capacitor/core";
import { CameraOverlayPlugin } from "./definitions";

export class CameraOverlayWeb extends WebPlugin implements CameraOverlayPlugin {
    constructor() {
        super({
            name: "CameraOverlay",
            platforms: ["web"]
        });
    }

    async startCamera(): Promise<void> {
        return Promise.resolve();
    }
    async stopCamera(): Promise<void> {
        return Promise.resolve();
    }
}

const CameraOverlay = new CameraOverlayWeb();

export { CameraOverlay };
