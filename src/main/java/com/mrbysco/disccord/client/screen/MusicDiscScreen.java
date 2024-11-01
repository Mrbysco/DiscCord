package com.mrbysco.disccord.client.screen;

import com.mrbysco.disccord.DiscCordMod;
import com.mrbysco.disccord.network.PacketHandler;
import com.mrbysco.disccord.network.payload.SetRecordUrlMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class MusicDiscScreen extends Screen {
	private static final ResourceLocation TEXTURE = DiscCordMod.modLoc("textures/gui/record_input.png");
	private static final ResourceLocation TEXT_FIELD_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
	private EditBox nameField;

	private final int backgroundWidth = 176;
	private final int backgroundHeight = 44;
	private final String inputDefaultText;

	public MusicDiscScreen(Component title, String inputDefaultText) {
		super(title);
		this.inputDefaultText = inputDefaultText;
	}

	public static void openScreen(Component title, String inputDefaultText) {
		Minecraft.getInstance().setScreen(new MusicDiscScreen(title, inputDefaultText));
	}

	public void updateTextPosition() {
		if (font == null) {
			return;
		}

		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;

		this.nameField = new EditBox(font, x + 62, y + 18, 103, 12, (Component) Component.translatable((String) "container.repair"));
		this.nameField.setCanLoseFocus(false);
		this.nameField.setTextColor(-1);
		this.nameField.setTextColorUneditable(-1);
		this.nameField.setBordered(false);
		this.nameField.setMaxLength(200);
		this.nameField.setResponder(this::onRenamed);
		this.nameField.setValue(this.inputDefaultText);
		this.addWidget(this.nameField);
		this.setInitialFocus(this.nameField);
		this.nameField.setEditable(true);
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		super.resize(minecraft, width, height);

		String string = this.nameField.getValue();
		this.updateTextPosition();
		this.nameField.setValue(string);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_ENTER) {
			if (!this.nameField.getValue().equals(this.inputDefaultText)) {
				PacketHandler.CHANNEL.send(PacketDistributor.SERVER.noArg(), new SetRecordUrlMessage(this.nameField.getValue()));
			}

			this.minecraft.player.closeContainer();
		}
		if (this.nameField.keyPressed(keyCode, scanCode, modifiers) || this.nameField.canConsumeInput()) {
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	private void onRenamed(String s) {
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		graphics.blit(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
		graphics.blit(TEXT_FIELD_TEXTURE, x + 59, y + 14, 0, 182, 110, 16);

		if (this.nameField == null) {
			updateTextPosition();
		}

		this.nameField.render(graphics, mouseX, mouseY, partialTick);
	}
}
