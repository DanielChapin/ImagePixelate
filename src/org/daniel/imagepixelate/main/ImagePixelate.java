package org.daniel.imagepixelate.main;

import java.util.Scanner;

import org.daniel.imagepixelate.graphics.IPPanel;

public class ImagePixelate {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		new IPPanel(scanner.next());
	}

}
