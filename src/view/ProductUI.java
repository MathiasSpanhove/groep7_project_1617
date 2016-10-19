package view;

import java.awt.HeadlessException;

import javax.swing.JOptionPane;

import domain.Shop;
import domain.product.Product;
import exception.DatabaseException;
import exception.DomainException;
import exception.StateException;

public class ProductUI {
	Shop shop;
	ShopUI shopUI;

	public ProductUI(Shop shop, ShopUI shopUI) {
		this.shop = shop;
		this.shopUI = shopUI;
	}

	public void showMenu() {
		int choice = -1;
		String menu = "1. Add product" + "\n2. Show product" + "\n3. Show rental price" + "\n4. Show all products"
				+ "\n5. Check product state" + "\n6. Borrow product" + "\n7. Return product" + "\n8. Repair product"
				+ "\n9. Remove product" + "\n\n0. Back";

		while (choice != 0) {
			try {
				String choiceString = JOptionPane.showInputDialog(menu);
				if (choiceString == null) {
					break;
				} else {
					choice = Integer.parseInt(choiceString);

					switch (choice) {
					case 0:
						break;
					case 1:
						addProduct();
						break;
					case 2:
						showProduct();
						break;
					case 3:
						showPrice();
						break;
					case 4:
						showAllProducts();
						break;
					case 5:
						showProductState();
						break;
					case 6:
						borrowProduct();
						break;
					case 7:
						returnProduct();
						break;
					case 8:
						repairProduct();
						break;
					case 9:
						deleteProduct();
						break;
					default:
						JOptionPane.showMessageDialog(null, "Please enter a valid number");
					}
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Please enter a number");
				e.printStackTrace();
			}
		}
	}

	protected void addProduct() {
		try {
			String title = JOptionPane.showInputDialog(null, "Enter the title:", "Add Product",
					JOptionPane.PLAIN_MESSAGE);
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Add Product", JOptionPane.PLAIN_MESSAGE));
			Character type = JOptionPane.showInputDialog(null, "Enter the type (M for movie/G for game/C for CD):",
					"Add Product", JOptionPane.PLAIN_MESSAGE).charAt(0);

			shop.addProduct(id, title, type);
			JOptionPane.showMessageDialog(null, "Product succesfully added to the database");
		} catch (DomainException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void showProduct() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Show Product", JOptionPane.PLAIN_MESSAGE));
			JOptionPane.showMessageDialog(null, shop.getProduct(id).getTitle());
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void showPrice() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Show Price", JOptionPane.PLAIN_MESSAGE));
			int days = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter number of days:", "Show Price",
					JOptionPane.PLAIN_MESSAGE));
			JOptionPane.showMessageDialog(null, shop.getProduct(id).getPrice(days));
		} catch (DomainException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void showAllProducts() {
		try {
			JOptionPane.showMessageDialog(null, shop.productsToString());
		} catch (HeadlessException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void showProductState() {
		try {
			int id = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the id:", "Show Product State",
					JOptionPane.PLAIN_MESSAGE));
			JOptionPane.showMessageDialog(null, "This product is " + shop.getProduct(id).getCurrentState().toString());
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void borrowProduct() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Borrow Product", JOptionPane.PLAIN_MESSAGE));
			Product p = shop.getProduct(id);
			p.borrowProduct();
			shop.updateProduct(p);
		} catch (StateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void returnProduct() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Return Product", JOptionPane.PLAIN_MESSAGE));
			Product p = shop.getProduct(id);

			int n = JOptionPane.showConfirmDialog(null, "Is the product damaged?", "Return product",
					JOptionPane.YES_NO_OPTION);
			boolean damaged = (n == JOptionPane.YES_OPTION);
			p.returnProduct(damaged);
			shop.updateProduct(p);
			double fine = shop.calculateFine(p.getLastBorrowed());

			if (fine > 0.0) {
				JOptionPane.showMessageDialog(null, "Please pay the fine of �" + fine);
			}
		} catch (StateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void repairProduct() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Repair Product", JOptionPane.PLAIN_MESSAGE));
			Product p = shop.getProduct(id);
			p.repairProduct();
			shop.updateProduct(p);
		} catch (StateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}

	protected void deleteProduct() {
		try {
			int id = Integer.parseInt(
					JOptionPane.showInputDialog(null, "Enter the id:", "Remove Product", JOptionPane.PLAIN_MESSAGE));

			int n = JOptionPane.showConfirmDialog(null,
					"Are you sure that you want to permanently remove this product?", "Remove Product",
					JOptionPane.YES_NO_OPTION);

			if (n == JOptionPane.YES_OPTION) {
				Product p = shop.getProduct(id);
				p.deleteProduct();
				shop.deleteProduct(id);
				JOptionPane.showMessageDialog(null, "Product succesfully removed from the database");
			}
		} catch (StateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (DatabaseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			e.printStackTrace();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Input not valid");
			e.printStackTrace();
		}
	}
}
