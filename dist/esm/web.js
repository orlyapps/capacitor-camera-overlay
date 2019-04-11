var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
import { WebPlugin } from "@capacitor/core";
export class CameraOverlayWeb extends WebPlugin {
    constructor() {
        super({
            name: "CameraOverlay",
            platforms: ["web"]
        });
    }
    startCamera() {
        return __awaiter(this, void 0, void 0, function* () {
            return Promise.resolve();
        });
    }
    stopCamera() {
        return __awaiter(this, void 0, void 0, function* () {
            return Promise.resolve();
        });
    }
}
const CameraOverlay = new CameraOverlayWeb();
export { CameraOverlay };
//# sourceMappingURL=web.js.map